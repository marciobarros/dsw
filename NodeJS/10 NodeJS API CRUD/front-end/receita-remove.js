Vue.component('deleta-receita', {
    props: ['controlador'],
    
    data: function () {
        return { 
            headers: [
                { text: 'Ingrediente', align: 'start', sortable: true, value: 'item' },
                { text: 'Quantidade', align: 'start', sortable: true, value: 'qtde' },
            ]
        }
    },
    
    template: `
        <div>
            <v-container>
                <v-row>
                    <v-col cols="9">
                        <h2>Remove receita</h2>
                    </v-col>
                    <v-col cols="3" class="text-right">
                        <v-btn color="primary" class="mb-2" @click="removeReceita">
                            Remove
                        </v-btn>
                        <v-btn color="outlined" class="mb-2" @click="retornaLista">
                            Retorna para a lista
                        </v-btn>
                    </v-col>
                </v-row>
            </v-container>

            <v-container>
                <v-row>
                    <v-col cols="12">
                        <p class="view-label">Nome:</p>
                        <p class="view-data">{{controlador.itemSelecionado.nome}}</p>

                        <p class="view-label">Modo de preparo:</p>
                        <p class="view-data">{{controlador.itemSelecionado.preparo}}</p>

                        <p class="view-label">Ingredientes:</p>
                        <v-data-table :headers="headers" :items="controlador.itemSelecionado.ingredientes" hide-default-footer class="elevation-1"></v-data-table>
                    </v-col>
                </v-row>
            </v-container>
        </div>`,

    methods: {
        removeReceita: function() {
            axios.delete("http://localhost:3000/receitas/" + this.controlador.itemSelecionado.id).then(response => {
                this.controlador.lista();
            })
        },

        retornaLista: function() {
            this.controlador.lista();
        }
    }
})