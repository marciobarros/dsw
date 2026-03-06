<template lang="html">
  <v-row v-show="!$root.credentials" class="mt-8">
    <v-col offset-md="4" md="4">
      <h2 class="form-title">Recuperar senha</h2>
      <h5 class="form-subtitle mb-8">Entre com o seu e-mail no formulário abaixo. Um link para troca de senha será enviado por e-mail.</h5>

      <p class="error pa-1 mb-8" v-if="error">{{error}}</p>

      <v-form @submit.prevent="processForm">
        <v-text-field v-model="form.email" label="Entre o seu e-mail" ></v-text-field>
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
        error: ''
      }
    },

    methods: {
      processForm: function() {
        axios.post(this.$root.config.url + "/usuarios/esqueci", this.form)
          .then(() => {
            this.$router.replace('token-sent');
            this.error = '';
          })
          .catch(error => {
            if (error.response) {
              this.error = error.response.data.message;
            }
            else {
              this.error = 'Ocorreu um problema ao enviar o e-mail de recuperação de senha.';
            }
          })
      }
    }
  }
</script>
