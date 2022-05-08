import Vue from 'vue'
import Vuex from 'vuex'
import factory from './modules/factory'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    factory
  }
})

export default store
