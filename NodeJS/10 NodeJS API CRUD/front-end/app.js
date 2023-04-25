new Vue({
    el: "#app",
    vuetify: new Vuetify(),

    component: ['lista-receitas', 'deleta-receita', 'formulario-receita'],

    data: {
        drawer: false,
        controladorCadastroReceitas: criaControladorCRUD()
    },

    methods: {
        toggleDrawer: function() {
            this.drawer = !this.drawer;
        }
    },

    mounted: function() {
        this.controladorCadastroReceitas.painelLista = this.$refs.lista;
        this.controladorCadastroReceitas.painelFormulario = this.$refs.formulario;
    }
})