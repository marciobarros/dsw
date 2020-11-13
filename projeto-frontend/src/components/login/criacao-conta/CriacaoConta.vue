<template lang="html">
  <div class="criacao-conta row" v-if="!this.$root.credentials">
    <div class="col-md-4 col-md-offset-4 text-left">
      <h2 class="form-title">Criar conta</h2>
      <h6 class="form-subtitle">Entre com os dados abaixo para se registrar no sistema.</h6>
      
      <form @submit.prevent="processForm">
        <div class="form-group">
          <label for="name">Nome</label>
          <input type="text" class="form-control" id="name" placeholder="Entre o seu nome" v-model="form.nome"></input>
          <span class="error" v-if="error.nome">{{error.nome}}</span>
        </div>

        <div class="form-group">
          <label for="email">E-mail</label>
          <input type="text" class="form-control" id="email" placeholder="Entre o seu e-mail" v-model="form.email"></input>
          <span class="error" v-if="error.email">{{error.email}}</span>
        </div>

        <div class="form-group">
          <label for="password">Senha</label>
          <input type="password" class="form-control" id="password" placeholder="Entre com uma senha" v-model="form.senha"></input>
          <span class="error" v-if="error.senha">{{error.senha}}</span>
        </div>

        <div class="form-group">
          <label for="password-repeat">Repita a senha</label>
          <input type="password" class="form-control" id="password-repeat" placeholder="Repita a sua senha" v-model="form.senhaRepetida"></input>
          <span class="error" v-if="error.senhaRepetida">{{error.senhaRepetida}}</span>
        </div>

        <button type="submit" class="btn btn-primary">Cria conta</button>
      </form>

      <div class="link-login">
        <router-link class="link" :to="{ name: 'login' }">
          Já tenho uma conta
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from 'axios';

  export default {
    data() {
      return {
        form: {
          nome: "",
          email: "",
          senha: "",
          senhaRepetida: "",
        },
        error: { }
      }
    },

    methods: {
      processForm: function() {
        axios.post(this.$root.config.url + "/api/usuario/novo", this.form)
          .then(response => {
            this.$router.replace('account-created');
            this.error = {};
          })
          .catch(error => {
            this.error = error.response.data.errors;
          });
      }
    }
  }
</script>

<style lang="css" scoped>
div.criacao-conta {
  margin-top: 32px;
}
div.link-login {
  margin-top: 32px;
}
</style>