<template>
  <v-app>
    <v-navigation-drawer app v-model="drawer">
        <v-list nav dense>
            <v-list-item-group active-class="deep-purple--text text--accent-4">
                <v-list-item>
                    <v-list-item-title @click="listaReceitas">Receitas</v-list-item-title>
                </v-list-item>
            </v-list-item-group>
        </v-list>
    </v-navigation-drawer>
    
    <v-app-bar app color="deep-purple accent-4" dark>
        <v-app-bar-nav-icon @click.stop="toggleDrawer" v-if="$root.credentials"></v-app-bar-nav-icon>
        <v-toolbar-title>Receituário VueJS</v-toolbar-title>
        <v-spacer></v-spacer>
        <span v-if="$root.credentials">
          Olá, {{$root.credentials.nome}}.
          <router-link :to="{ name: 'change-password' }" replace>
            <v-btn icon><v-icon>mdi-lock-reset</v-icon></v-btn>
          </router-link>          
          <v-btn icon @click="logout"><v-icon>mdi-logout</v-icon></v-btn>
        </span>
    </v-app-bar>
  
    <v-main>
      <router-view></router-view>
    </v-main>
  
    <v-footer app>
        @BSI UNIRIO: Desenvolvimento de Servidor Web @2023
    </v-footer>
  </v-app>
</template>

<script>
export default {
  name: 'App',

  data: () => ({
    drawer: false
  }),

  methods: {
    toggleDrawer: function() {
        this.drawer = !this.drawer;
    },

    logout: function() {
      this.$root.credentials = null;
      this.$router.replace('/');
    },

    listaReceitas: function() {
      this.$router.replace('/receitas');      
    }
  },
};
</script>

<style>
p.error {
    color: white !important;
}
.v-data-table__wrapper {
    border-top: 1px solid #ccc;
}
</style>