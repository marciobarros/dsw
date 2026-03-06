function criaControladorCRUD() {
    return {
        itemSelecionado: { },

        setItemSelecionado: function(item) {
            this.itemSelecionado = item;
        }
    }
}

module.exports = { criaControladorCRUD };