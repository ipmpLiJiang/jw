import Vue from 'vue'
import Vuex from 'vuex'
 
Vue.use(Vuex)
const key = 'check'
const store = new Vuex.Store({
  state () {
    return {
      user: null,
      YdyfRole:'',
     
    }
  },
  getters: {
    getStorage: function (state) {
      if (!state.user) {
        state.user = JSON.parse(localStorage.getItem(key))
      }
      return state.user
    },
    YdyfStorage: function (state) {
      if (!state.YdyfRole) {
    
        state.YdyfRole = JSON.parse(localStorage.getItem('YdyfRole'))
      }
    
      return state.YdyfRole
    },
 
  },
  mutations: {
    $_setStorage (state, value) {
      state.user = value
      localStorage.setItem(key, JSON.stringify(value))
    },
    $_removeStorage (state) {
      state.user = null
      console.log(key);
      localStorage.removeItem(key)
    },
  
  
  }
})
 
export default store