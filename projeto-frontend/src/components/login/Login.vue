<template lang="html">
  <div class="login row" v-if="!$root.credentials">
    <div class="col-md-4 col-md-offset-4 text-left">
      <h2 class="form-title">Login</h2>
      <h6 class="form-subtitle">Entre com as suas credenciais para o login.</h6>

      <form @submit.prevent="processForm">
        <div class="form-group">
          <label for="username">Login</label>
          <input type="text" class="form-control" id="username" placeholder="Entre o seu login" v-model="form.email">
          <span class="error" v-if="error.all">{{error.all}}</span>
        </div>

        <div class="form-group">
          <label for="password">Senha</label>
          <input type="password" class="form-control" id="password" placeholder="Entre e sua senha" v-model="form.senha">
        </div>

        <button type="submit" class="btn btn-primary">Envia</button>
      </form>

      <div class="link-recuperar-senha">
        <router-link class="link" :to="{ name: 'forgot-password' }" replace>
          Recuperar a minha senha
        </router-link>
      </div>
      
      <div class="link-criar-conta">
        <router-link class="link" :to="{ name: 'create-account' }" replace>
          Criar nova conta
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
        form: { email: "", senha: "" },
        error: { }
      }
    },

    methods: {
      processForm: function() {
        axios.post(this.$root.config.url + "/auth", this.form)
          .then(response => {
            this.$root.credentials = response.data.data;
            this.$router.replace('/');
            this.error = {};
          })
          .catch(error => {
            this.error = error.response.data.errors;
          })
      }
    }
  }
</script>

<style lang="css" scoped>
div.login {
  margin-top: 32px;
}
div.link-recuperar-senha {
  margin-top: 32px;
}
div.link-criar-conta {
  margin-top: 8px;
}
</style>