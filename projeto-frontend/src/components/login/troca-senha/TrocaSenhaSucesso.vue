<template lang="html">
  <div class="senha-trocada row">
    <div class="col-md-4 col-md-offset-4 text-left">
      <h2 class="form-title">Senha trocada!</h2>
      <h6 class="form-subtitle">A sua senha foi trocada. Você deve utilizar esta nova senha para acessar o sistema nas próximas vezes.</h6>
      
      <div class="link-home">
        <router-link class="link" :to="{ name: 'home' }">
          Ir para a homepage.
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
div.senha-trocada {
  margin-top: 32px;
}
div.link-home {
  margin-top: 32px;
}
</style>