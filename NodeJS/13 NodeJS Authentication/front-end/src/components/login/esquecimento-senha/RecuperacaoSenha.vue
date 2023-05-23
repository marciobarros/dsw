<template lang="html">
  <v-row v-show="!$root.credentials" class="mt-8">
    <v-col offset-md="4" md="4">
      <h2 class="form-title">Reinicialização de senha</h2>
      <h5 class="form-subtitle mb-8">Entre com a nova senha no formulário abaixo.</h5>
      
      <p class="error pa-1 mb-8" v-if="error">{{error}}</p>

      <v-form @submit.prevent="processForm">
        <v-text-field v-model="form.senha" type="password" label="Entre com a sua nova senha" ></v-text-field>
        <v-text-field v-model="form.senhaRepetida" type="password" label="Repita a sua senha" ></v-text-field>
        <v-btn type="submit" class="primary">Troca Senha</v-btn>
      </v-form>

      <div class="mt-12">
        <router-link :to="{ name: 'login' }">
          Lembrei da minha senha e não quero mais trocar
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
        form: { 
          token: this.$route.query.token, 
          email: this.$route.query.email, 
          senha: "", 
          senhaRepetida: "" 
        },
        error: ''
      }
    },

    methods: {
      processForm: function() {
        axios.post(this.$root.config.url + "/usuarios/reset", this.form)
          .then(() => {
            this.$router.replace('reseted');
            this.error = '';
          })
          .catch(error => {
            this.error = error.response.data.message;
          });
      }
    }
  }
</script>
