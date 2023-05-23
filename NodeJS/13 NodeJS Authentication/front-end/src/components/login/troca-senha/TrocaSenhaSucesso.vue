<template lang="html">
  <v-row v-show="$root.credentials" class="mt-8">
    <v-col offset-md="4" md="4">
      <h2 class="form-title">Senha trocada!</h2>
      <h5 class="form-subtitle mb-8">A sua senha foi trocada. Você deve utilizar esta nova senha para acessar o sistema nas próximas vezes.</h5>
      
      <div class="mb-2">
        <router-link class="link" :to="{ name: 'home' }">
          Ir para a homepage.
        </router-link>
      </div>
    </v-col>
  </v-row>
</template>

<script>
  export default {
  }
</script>
