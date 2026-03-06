<template lang="html">
  <v-row v-show="!$root.credentials" class="mt-8">
    <v-col offset-md="4" md="4">
      <h2 class="form-title">Token enviado!</h2>
      <h5 class="form-subtitle mb-8">Um token para recuperação de senha foi enviado para o seu e-mail. Clique no link deste e-mail para entrar com uma nova senha.</h5>

      <div class="mt-12">
        <router-link class="link" :to="{ name: 'login' }">
          Ir para a página de login.
        </router-link>
      </div>
    </v-col>
  </v-row>
</template>

<script>
  export default {
  }
</script>
