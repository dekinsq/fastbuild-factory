import axios from 'axios'
import qs from 'qs'

axios.defaults.baseURL = process.env.VUE_APP_BASE_API
axios.defaults.timeout = 30 * 1000 // 30s request time out

let http = {}

http.axios = function (options) {
  return new Promise((resolve, reject) => {
    axios(options).then(res => {
      resolve(res.data, res)
    }).catch(err => {
      reject(err)
    })
  })
}

http.post = function (url, params) {
  return http.axios({
    url: url,
    method: 'post',
    headers: {'Content-Type': 'application/json;charset=UTF-8', 'Content-Language': 'zh-cn'},
    data: params
  })
}

http.get = function (url, params) {
  return http.axios({
    url: url,
    method: 'get',
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    params: params
  })
}

http.submit = function (url, params) {
  return http.axios({
    url: url,
    method: 'post',
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    data: qs.stringify(params)
  })
}

http.upload = function (url, params) {
  return http.axios({
    url: url,
    method: 'post',
    headers: {'Content-Type': 'multipart/form-data'},
    data: params
  })
}

http.download = function (url, params, onDownloadProgress) {
  return http.axios({
    url: url,
    method: 'post',
    data: params,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    responseType: 'blob',
    onDownloadProgress: onDownloadProgress
  })
}

export default http
