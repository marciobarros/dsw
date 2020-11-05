import Vue from 'vue'
import Router from 'vue-router'
import App from './App.vue'
import Homepage from './components/Homepage.vue'
import Login from './components/login/Login.vue'

/* New account creation */
import CreateAccount from './components/login/create-account/CreateAccount.vue'
import AccountCreated from './components/login/create-account/AccountCreated.vue'

/* Password forgot & reset */
import ForgotPassword from './components/login/forgot-password/ForgotPassword.vue'
import TokenSent from './components/login/forgot-password/TokenSent.vue'
import ResetPassword from './components/login/forgot-password/ResetPassword.vue'
import ResetedPassword from './components/login/forgot-password/ResetedPassword.vue'

Vue.use(Router)

const router = new Router({
 routes: [
   {
     path: '/',
     name: 'home',
     component: Homepage
   },
   {
     path: '/login',
     name: 'login',
     component: Login,
   },
   {
    path: '/login/new',
    name: 'create-account',
    component: CreateAccount,
  },
  {
   path: '/login/account-created',
   name: 'account-created',
   component: AccountCreated,
  },
  {
    path: '/login/forgot',
    name: 'forgot-password',
    component: ForgotPassword,
  },
  {
    path: '/login/token-sent',
    name: 'token-sent',
    component: TokenSent,
  },  
  {
    path: '/login/reset',
    name: 'reset-password',
    component: ResetPassword,
  },
  {
    path: '/login/reseted',
    name: 'password-reseted',
    component: ResetedPassword,
  },
]
})

new Vue({
  data: {
    credentials: null,
    config: {
      url: "http://localhost:8080"
    }
  },

 el: '#app',
 render: h => h(App),
 router
})