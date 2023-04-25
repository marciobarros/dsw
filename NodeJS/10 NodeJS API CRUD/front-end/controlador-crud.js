function criaControladorCRUD() {
    return {
        painelLista: null,

        painelFormulario: null,

        apresentandoPainelLista: true,

        apresentandoPainelFormulario: false,

        apresentandoPainelRemocao: false,

        itemSelecionado: { },

        lista: function() {
            this.painelLista.carregaReceitas();
            this.apresentandoPainelLista = true;
            this.apresentandoPainelFormulario = false;
            this.apresentandoPainelRemocao = false;
        },

        insere: function(item) {
            this.itemSelecionado = item;
            this.painelFormulario.prepara();
            this.apresentandoPainelLista = false;
            this.apresentandoPainelFormulario = true;
            this.apresentandoPainelRemocao = false;
        },

        edita: function(item) {
            this.itemSelecionado = item;
            this.painelFormulario.prepara();
            this.apresentandoPainelLista = false;
            this.apresentandoPainelFormulario = true;
            this.apresentandoPainelRemocao = false;
        },

        remove: function(item) {
            this.itemSelecionado = item;
            this.apresentandoPainelLista = false;
            this.apresentandoPainelFormulario = false;
            this.apresentandoPainelRemocao = true;
        }
    }
}