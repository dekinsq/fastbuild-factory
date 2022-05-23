import store from '@/store'

let config = store.state.factory.config;

export function validRules (app, types) {
  types = types || ['unique', 'webFramework', 'webUI', 'serverMode'];
  app.rules = app.rules || [];
  let msgs = [];
  let valids = [];
  if (types.indexOf('unique') > -1) valids.push(validUnique(app));
  if (types.indexOf('webFramework') > -1) valids.push(validWeb(app, 'webFramework'));
  if (types.indexOf('webUI') > -1) valids.push(validWeb(app, 'webUI'));
  if (types.indexOf('serverMode') > -1) valids.push(validWeb(app, 'serverMode'));

  for (let vd of valids) {
    if (vd.valid) {
      msgs.push(vd.msg);
    }
  }
  return {
    title: app.title,
    disable: msgs.length > 0,
    assertMsg: msgs.join('、')
  };
}

export function validUnique (app) {
  for (let rule of app.rules) {
    if (rule.type == 'unique') {
      if (config.appList && config.appList.length > 0) {
        if (config.appList.findIndex(res => {
          return res.appId != app.appId && res.appType == rule.value;
        }) > -1) {
          return {
            valid: true,
            msg: rule.msg
          }
        }
      }
    }
  }
  return { valid: false };
}

export function validWeb (app, key) {
  for (let rule of app.rules) {
    if (rule.type == key && rule.value.indexOf(config[key]) < 0) {
      return {
        valid: true,
        msg: rule.msg
      }
    }
  }
  return { valid: false };
}
