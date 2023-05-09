<template lang="html">
  <v-row v-show="!$root.credentials" class="mt-8">
    <v-col offset-md="4" md="4">
      <h2 class="form-title">Reinicialização de senha</h2>
      <h5 class="form-subtitle mb-8">Entre com a nova senha no formulário abaixo.</h5>
      
      <v-form @submit.prevent="processForm">
        <div class="form-group">
          <v-text-field v-model="form.senha" type="password" label="Entre com a sua nova senha" ></v-text-field>
          <span class="error" v-if="error.senha">{{error.senha}}</span>
        </div>

        <div class="form-group">
          <v-text-field v-model="form.senhaRepetida" type="password" label="Repita a sua senha" ></v-text-field>
          <span class="error" v-if="error.senhaRepetida">{{error.senhaRepetida}}</span>
        </div>

        <v-btn type="submit" class="primary">Troca Senha</v-btn>
      </v-form>

      <div>
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
        form: { token: this.$route.query.token, email: this.$route.query.email, senha: "", senhaRepetida: "" },
        error: { }
      }
    },

    methods: {
      processForm: function() {
        axios.post(this.$root.config.url + "/api/usuario/reset", this.form)
          .then(() => {
            this.$router.replace('reseted');
            this.error = {};
          })
          .catch(error => {
            this.error = error.response.data.errors;
          });
      }
    }
  }
</script>
