<template lang="html">
  <v-row v-show="!$root.credentials" class="mt-8">
    <v-col offset-md="4" md="4">
      <h2 class="form-title">Recuperar senha</h2>
      <h5 class="form-subtitle mb-8">Entre com o seu e-mail no formulário abaixo. Um link para troca de senha será enviado por e-mail.</h5>

      <v-form @submit.prevent="processForm">
        <div class="form-group">
          <v-text-field v-model="form.email" label="Entre o seu e-mail" ></v-text-field>
          <span class="error" v-if="error.email">{{error.email}}</span>
        </div>

        <v-btn type="submit" class="primary">Envia</v-btn>
      </v-form>

      <div class="mt-12">
        <router-link class="link" :to="{ name: 'login' }">
          Lembrou sua senha?
        </router-link>
      </div>
      
      <div class="mb-2">
        <router-link class="link" :to="{ name: 'create-account' }">
          Criar nova conta
        </router-link>
      </div>
    </v-col>
  </v-row>
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
          .then(() => {
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