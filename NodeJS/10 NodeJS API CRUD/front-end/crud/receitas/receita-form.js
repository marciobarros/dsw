Vue.component('formulario-receita', {
    props: ['controlador'],
    
    data: function () {
        return { 
            headers: [
                { text: 'Ingrediente', align: 'start', sortable: true, value: 'item' },
                { text: 'Quantidade', align: 'start', sortable: true, value: 'qtde' },
                { text: '', value: 'actions', sortable: false },
            ],
            novoIngrediente: {
                item: '',
                qtde: ''
            },
            titulo: 'Nova receita',
            errorMessage: ''
        }
    },
    
    template: `
        <div>
            <v-container>
                <v-row>
                    <v-col cols="6">
                        <h2>{{titulo}}</h2>
                    </v-col>
                    <v-col cols="6" class="text-right">
                        <v-btn color="primary" class="mb-2" @click="salvaReceita">
                            Salva
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
                        <p class="error" v-show="errorMessage != ''">{{errorMessage}}</p>

                        <label class="form-label" for="nome">Nome:</label>
                        <input class="form-input" name="nome" v-model="controlador.itemSelecionado.nome"/>

                        <label class="form-label" for="preparo">Preparo:</label>
                        <textarea class="form-input" name="preparo" rows="3" v-model="controlador.itemSelecionado.preparo"/>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col cols="9">
                        <v-data-table :headers="headers" :items="controlador.itemSelecionado.ingredientes" hide-default-footer class="elevation-1">
                            <template v-slot:item.actions="{item}">
                                <div class="text-right">
                                    <v-icon @click="removeIngrediente(item)">mdi-delete</v-icon>
                                </div>
                            </template>
                        </v-data-table>
                    </v-col>

                    <v-col cols="3">
                        <div class="detail-form">
                            <label class="form-label" for="ingrediente-nome">Ingrediente:</label>
                            <input class="form-input" name="ingrediente-nome" v-model="novoIngrediente.item"/>
    
                            <label class="form-label" for="ingrediente-qtde">Quantidade:</label>
                            <input class="form-input" name="ingrediente-qtde" v-model="novoIngrediente.qtde"/>

                            <br/>

                            <v-btn color="primary" class="mt-8" @click="adicionaIngrediente" :disabled="novoIngrediente.item === '' || novoIngrediente.qtde === ''">
                                Adiciona Ingrediente
                            </v-btn>
                        </div>
                    </v-col>
                </v-row>
            </v-container>
        </div>`,

    methods: {
        prepara: function() {
            this.errorMessage = "";
            this.titulo = (this.controlador.itemSelecionado.id === -1) ? "Nova receita" : "Edita Receita";
        },

        salvaReceita: function() {
            axios.post("http://localhost:3000/receitas/", this.controlador.itemSelecionado).then(response => {
                this.errorMessage = "";
                this.controlador.lista();
            }).catch(error => {
                this.errorMessage = error.response.data.message;
            });
        },

        retornaLista: function() {
            this.controlador.lista();
        },

        adicionaIngrediente: function() {
            var ingrediente = {
                item: this.novoIngrediente.item,
                qtde: this.novoIngrediente.qtde
            }

            this.controlador.itemSelecionado.ingredientes.push(ingrediente);
            this.novoIngrediente = { item: '', qtde: '' };
        },

        removeIngrediente: function(ingrediente) {
            var index = this.controlador.itemSelecionado.ingredientes.indexOf(ingrediente);
            this.controlador.itemSelecionado.ingredientes.splice(index, 1);
        }
    }
})