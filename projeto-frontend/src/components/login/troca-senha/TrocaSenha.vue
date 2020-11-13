<template lang="html">
  <div class="troca-senha row" v-if="this.$root.credentials">
    <div class="col-md-4 col-md-offset-4 text-left">
      <h2 class="form-title">Troca de senha</h2>
      <h6 class="form-subtitle">Entre com a senha atual e a nova senha no formulário abaixo.</h6>
      
      <form @submit.prevent="processForm">
        <div class="form-group">
          <label for="password">Nova senha</label>
          <input type="password" class="form-control" id="password" placeholder="Entre com a senha atual" v-model="form.senhaAntiga"></input>
          <span class="error" v-if="error.senhaAntiga">{{error.senhaAntiga}}</span>
        </div>

        <div class="form-group">
          <label for="password">Nova senha</label>
          <input type="password" class="form-control" id="password" placeholder="Entre com uma senha" v-model="form.senhaNova"></input>
          <span class="error" v-if="error.senhaNova">{{error.senhaNova}}</span>
        </div>

        <div class="form-group">
          <label for="password-repeat">Repita a nova senha</label>
          <input type="password" class="form-control" id="password-repeat" placeholder="Repita a sua senha" v-model="form.senhaNovaRepetida"></input>
          <span class="error" v-if="error.senhaNovaRepetida">{{error.senhaNovaRepetida}}</span>
        </div>

        <button type="submit" class="btn btn-primary">Troca Senha</button>
      </form>
    </div>
  </div>
</template>

<script>
  import axios from 'axios';

  export default {
    data() {
      return {
        form: { token: this.$route.query.token, senhaAntiga: "", senhaNova: "", senhaNovaRepetida: "" },

        error: { },
        
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
        axios.post(this.$root.config.url + "/api/usuario/trocaSenha", this.form, this.httpOptions)
          .then(response => {
            this.$router.replace('changed');
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
div.troca-senha {
  margin-top: 32px;
}
</style>