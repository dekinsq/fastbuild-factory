import Vue from 'vue'
import Router from 'vue-router'
import Factory from '@/pages/factory'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Factory',
      component: Factory
    }
  ]
})
