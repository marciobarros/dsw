<template lang="html">
  <div class="senha-reinicializada row">
    <div class="col-md-4 col-md-offset-4 text-left">
      <h2 class="form-title">Senha trocada!</h2>
      <h6 class="form-subtitle">A sua senha foi trocada. Você pode utilizar esta nova senha para acessar o sistema.</h6>
      
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
div.senha-reinicializada {
  margin-top: 32px;
}
div.link-login {
  margin-top: 32px;
}
</style>