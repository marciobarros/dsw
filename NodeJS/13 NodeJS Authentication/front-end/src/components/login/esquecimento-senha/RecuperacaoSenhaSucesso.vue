<template lang="html">
  <v-row v-show="!$root.credentials" class="mt-8">
    <v-col offset-md="4" md="4">
      <h2 class="form-title">Senha trocada!</h2>
      <h5 class="form-subtitle mb-8">A sua senha foi trocada. Você pode utilizar esta nova senha para acessar o sistema.</h5>
      
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
