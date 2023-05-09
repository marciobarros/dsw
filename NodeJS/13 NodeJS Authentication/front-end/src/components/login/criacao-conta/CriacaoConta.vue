<template lang="html">
  <v-row v-show="!$root.credentials" class="mt-8">
    <v-col offset-md="4" md="4">
      <h2 class="form-title">Criar conta</h2>
      <h5 class="form-subtitle mb-8">Entre com os dados abaixo para se registrar no sistema.</h5>
      
      <v-form @submit.prevent="processForm">
        <div class="form-group">
          <v-text-field v-model="form.nome" label="Entre o seu nome" ></v-text-field>
          <span class="error" v-if="error.nome">{{error.nome}}</span>
        </div>

        <div class="form-group">
          <v-text-field v-model="form.nome" label="Entre o seu e-mail" ></v-text-field>
          <span class="error" v-if="error.email">{{error.email}}</span>
        </div>

        <div class="form-group">
          <v-text-field v-model="form.senha" type="password" label="Entre a sua senha" ></v-text-field>
          <span class="error" v-if="error.senha">{{error.senha}}</span>
        </div>

        <div class="form-group">
          <v-text-field v-model="form.senhaRepetida" type="password" label="Repita a senha" ></v-text-field>
          <span class="error" v-if="error.senhaRepetida">{{error.senhaRepetida}}</span>
        </div>

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
        error: { }
      }
    },

    methods: {
      processForm: function() {
        axios.post(this.$root.config.url + "/api/usuario/novo", this.form)
          .then(() => {
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
