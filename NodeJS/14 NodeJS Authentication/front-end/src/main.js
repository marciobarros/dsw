import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import Router from 'vue-router'
import ControladorCRUD from './assets/controlador-crud.js'

/* Login e homepage */
import HomePage from './components/HomePage.vue'
import FormLogin from './components/login/login/FormLogin.vue'

/* Criacao de conta */
import CriacaoConta from './components/login/criacao-conta/CriacaoConta.vue'
import CriacaoContaSucesso from './components/login/criacao-conta/CriacaoContaSucesso.vue'

/* Esquecimento de senha */
import EsquecimentoSenha from './components/login/esquecimento-senha/EsquecimentoSenha.vue'
import EsquecimentoSenhaSucesso from './components/login/esquecimento-senha/EsquecimentoSenhaSucesso.vue'
import RecuperacaoSenha from './components/login/esquecimento-senha/RecuperacaoSenha.vue'
import RecuperacaoSenhaSucesso from './components/login/esquecimento-senha/RecuperacaoSenhaSucesso.vue'

/* Troca de senha */
import TrocaSenha from './components/login/troca-senha/TrocaSenha.vue'
import TrocaSenhaSucesso from './components/login/troca-senha/TrocaSenhaSucesso.vue'

/* Receitas */
import ReceitaLista from './components/receitas/ReceitaLista.vue'
import ReceitaFormulario from './components/receitas/ReceitaFormulario.vue'
import ReceitaRemove from './components/receitas/ReceitaRemove.vue'

/* Controlador do cadastro de receitas */
var controladorCadastroReceitas = ControladorCRUD.criaControladorCRUD();

/* Configuracao do router */
Vue.config.productionTip = false
Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
   {
     path: '/',
     name: 'home',
     component: HomePage
   },
   {
     path: '/login',
     name: 'login',
     component: FormLogin,
   },
   {
    path: '/login/new',
    name: 'create-account',
    component: CriacaoConta,
  },
  {
   path: '/login/account-created',
   name: 'account-created',
   component: CriacaoContaSucesso,
  },
  {
    path: '/login/forgot',
    name: 'forgot-password',
    component: EsquecimentoSenha,
  },
  {
    path: '/login/token-sent',
    name: 'token-sent',
    component: EsquecimentoSenhaSucesso,
  },  
  {
    path: '/login/reset',
    name: 'reset-password',
    component: RecuperacaoSenha,
  },
  {
    path: '/login/reseted',
    name: 'password-reseted',
    component: RecuperacaoSenhaSucesso,
  },  
  {
    path: '/login/change',
    name: 'change-password',
    component: TrocaSenha,
  },
  {
    path: '/login/changed',
    name: 'password-changed',
    component: TrocaSenhaSucesso,
  },
  {
    path: '/receitas',
    name: 'receitas-lista',
    component: ReceitaLista, 
    props: { 'controlador': controladorCadastroReceitas },
  },
  {
    path: '/receitas/edit',
    name: 'receitas-form',
    component: ReceitaFormulario, 
    props: { 'controlador': controladorCadastroReceitas },
  },
  {
    path: '/receitas/view',
    name: 'receitas-remove',
    component: ReceitaRemove, 
    props: { 'controlador': controladorCadastroReceitas },
  }
]})

/* Configuracao do Vue JS */
new Vue({
  data: {
    credentials: null,
    config: {
      url: "http://localhost:3000"
    }
  },

  el: '#app',
  render: h => h(App),
  vuetify,
  router
}).$mount('#app')
