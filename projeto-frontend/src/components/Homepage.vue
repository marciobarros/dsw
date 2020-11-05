<template lang="html">
  <div>
    <div class="login" v-if="!$root.credentials">
        <router-link class="link" :to="{ name: 'login' }">
        Login
        </router-link>
    </div>

    <div class="admin-commands" v-if="$root.credentials && $root.credentials.administrador">
        Olá administrador!
    </div>

    <div class="user-commands" v-if="$root.credentials && !$root.credentials.administrador">
        Olá usuário!
    </div>
  </div>
</template>

<script>
  export default {
  }
</script>

