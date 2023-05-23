<template lang="html">
  <v-row v-show="!$root.credentials" class="mt-8">
    <v-col offset-md="4" md="4">
      <h2 class="form-title">Criar conta</h2>
      <h5 class="form-subtitle mb-8">Entre com os dados abaixo para se registrar no sistema.</h5>

      <p class="error pa-1 mb-8" v-if="error">{{error}}</p>

      <v-form @submit.prevent="processForm">
          <v-text-field v-model="form.nome" label="Entre o seu nome" ></v-text-field>
          <v-text-field v-model="form.email" label="Entre o seu e-mail" ></v-text-field>
          <v-text-field v-model="form.senha" type="password" label="Entre a sua senha" ></v-text-field>
          <v-text-field v-model="form.senhaRepetida" type="password" label="Repita a senha" ></v-text-field>
          <v-btn type="submit" class="primary">Cria conta</v-btn>
      </v-form>

      <div class="mt-12">
        <router-link :to="{ name: 'login' }">
          Já tenho uma conta
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
          nome: "",
          email: "",
          senha: "",
          senhaRepetida: "",
        },
        error: ''
      }
    },

    methods: {
      processForm: function() {
        axios.post(this.$root.config.url + "/usuarios", this.form)
          .then(() => {
            this.$router.replace('account-created');
            this.error = '';
          })
          .catch(error => {
            this.error = error.response.data.message;
          });
      }
    }
  }
</script>
