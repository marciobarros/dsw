<template lang="html">
  <v-row v-show="$root.credentials" class="mt-8">
    <v-col offset-md="4" md="4">
      <h2 class="form-title">Troca de senha</h2>
      <h5 class="form-subtitle mb-8">Entre com a senha atual e a nova senha no formulário abaixo.</h5>
      
      <p class="error pa-1 mb-8" v-if="error">{{error}}</p>

      <v-form @submit.prevent="processForm">
        <v-text-field v-model="form.senhaAntiga" type="password" label="Entre com a senha atual" ></v-text-field>
        <v-text-field v-model="form.senhaNova" type="password" label="Entre com uma senha nova" ></v-text-field>
        <v-text-field v-model="form.senhaNovaRepetida" type="password" label="Repita a sua senha nova" ></v-text-field>
        <v-btn type="submit" class="primary">Troca Senha</v-btn>
      </v-form>
    </v-col>
  </v-row>
</template>

<script>
  import axios from 'axios';

  export default {
    data() {
      return {
        form: { 
          senhaAntiga: "", 
          senhaNova: "", 
          senhaNovaRepetida: "" 
        },

        error: '',
        
        httpOptions: {
            baseURL: this.$root.config.url,
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
              'Authorization': 'Bearer ' + this.$root.credentials.token
            }
        },
      }
    },

    methods: {
      processForm: function() {
        axios.post(this.$root.config.url + "/usuarios/trocaSenha", this.form, this.httpOptions)
          .then(() => {
            this.$router.replace('changed');
            this.error = '';
          })
          .catch(error => {
            this.error = error.response.data.message;
          });
      }
    }
  }
</script>
