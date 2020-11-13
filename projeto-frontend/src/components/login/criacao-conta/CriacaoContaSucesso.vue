<template lang="html">
  <div class="conta-criada row">
    <div class="col-md-4 col-md-offset-4 text-left">
      <h2 class="form-title">Conta registrada!</h2>
      <h6 class="form-subtitle">Faça o login para entrar no sistema.</h6>
      
      <div class="link-login">
        <router-link class="link" :to="{ name: 'login' }">
          Ir para a página de login.
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
  }
</script>

<style lang="css" scoped>
div.conta-criada {
  margin-top: 32px;
}
div.link-login {
  margin-top: 32px;
}
</style>