<template lang="html">
  <div class="recuperacao-senha row" v-if="!this.$root.credentials">
    <div class="col-md-4 col-md-offset-4 text-left">
      <h2 class="form-title">Recuperar senha</h2>
      <h6 class="form-subtitle">Entre com o seu e-mail no formulário abaixo. Um link para troca de senha será enviado por e-mail.</h6>

      <form  @submit.prevent="processForm">
        <div class="form-group">
          <label for="email">E-mail</label>
          <input type="text" class="form-control" id="email" placeholder="Entre o seu e-mail" v-model="form.email">
          <span class="error" v-if="error.email">{{error.email}}</span>
        </div>

        <button type="submit" class="btn btn-primary">Envia</button>
      </form>

      <div class="link-login">
        <router-link class="link" :to="{ name: 'login' }">
          Lembrou sua senha?
        </router-link>
      </div>
      
      <div class="link-criar-conta">
        <router-link class="link" :to="{ name: 'create-account' }">
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
        form: { email: "" },
        error: { }
      }
    },

    methods: {
      processForm: function() {
        axios.post(this.$root.config.url + "/api/usuario/esqueci", this.form)
          .then(response => {
            this.$router.replace('token-sent');
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
div.recuperacao-senha {
  margin-top: 32px;
}
div.link-login {
  margin-top: 32px;
}
div.link-criar-conta {
  margin-top: 8px;
}
</style>