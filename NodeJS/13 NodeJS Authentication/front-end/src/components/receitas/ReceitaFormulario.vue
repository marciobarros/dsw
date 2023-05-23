<template lang="html">
    <div v-show="$root.credentials" class="mt-8">
        <v-container>
            <v-row>
                <v-col cols="6">
                    <h2>{{titulo}}</h2>
                </v-col>
                <v-col cols="6" class="text-right">
                    <v-btn color="primary" class="mb-2 mr-2" @click="salvaReceita">
                        Salva
                    </v-btn>
                    <v-btn color="outlined" class="mb-2" @click="retornaLista">
                        Lista
                    </v-btn>
                </v-col>
            </v-row>
        </v-container>

        <v-container>
            <v-row>
                <v-col cols="12">
                    <p class="error pa-1 mb-8" v-show="errorMessage != ''">{{errorMessage}}</p>

                    <p class="view-label" v-show="item._id != ''">ID:</p>
                    <p class="view-data" v-show="item._id != ''">{{item._id}}</p>

                    <v-text-field v-model="item.nome" label="Entre o nome da receita"></v-text-field>
                    <v-text-field v-model="item.preparo" label="Entre o modo de preparo"></v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="9">
                    <v-data-table :headers="headers" :items="item.ingredientes" hide-default-footer class="elevation-1">
                        <template v-slot:[`item.actions`]="{item}">
                            <div class="text-right">
                                <v-icon @click="removeIngrediente(item)">mdi-delete</v-icon>
                            </div>
                        </template>
                    </v-data-table>
                </v-col>

                <v-col cols="3">
                    <div class="detail-form">
                        <h3 class="mb-4">Novo ingrediente</h3>
                        <v-text-field v-model="novoIngrediente.item" label="Entre o nome do ingrediente"></v-text-field>
                        <v-text-field v-model="novoIngrediente.qtde" label="Entre a quantidade"></v-text-field>

                        <v-btn color="primary" class="mt-8" @click="adicionaIngrediente" :disabled="novoIngrediente.item === '' || novoIngrediente.qtde === ''">
                            Adiciona Ingrediente
                        </v-btn>
                    </div>
                </v-col>
            </v-row>
        </v-container>
    </div>
</template>


<script>
  import axios from 'axios';

  export default {
    props: ['controlador'],

    data() {
        return {
            headers: [
                { text: 'Ingrediente', align: 'start', sortable: true, value: 'item' },
                { text: 'Quantidade', align: 'start', sortable: true, value: 'qtde' },
                { text: '', value: 'actions', sortable: false },
            ],

            item: null,

            novoIngrediente: {
                item: '',
                qtde: ''
            },

            titulo: '',
            errorMessage: '',
        
            httpOptions: {
                baseURL: this.$root.config.url,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$root.credentials.token
                }
            },
        }
    },

    methods: {
        prepara: function() {
            this.errorMessage = "";
            this.item = this.controlador.itemSelecionado;
            this.titulo = (this.item._id === "") ? "Nova receita" : "Edita Receita";
        },

        salvaReceita: function() {
            axios.post(this.$root.config.url + "/receitas/", this.item, this.httpOptions)
                .then(() => {
                    this.errorMessage = "";
                    this.$router.replace('/receitas');
                }).catch(error => {
                    this.errorMessage = error.response.data.message;
                });
        },

        retornaLista: function() {
            this.$router.replace('/receitas');
        },

        adicionaIngrediente: function() {
            var ingrediente = {
                item: this.novoIngrediente.item,
                qtde: this.novoIngrediente.qtde
            }

            this.item.ingredientes.push(ingrediente);
            this.novoIngrediente = { item: '', qtde: '' };
        },

        removeIngrediente: function(ingrediente) {
            var index = this.item.ingredientes.indexOf(ingrediente);
            this.item.ingredientes.splice(index, 1);
        }
    },

    created() {
        this.prepara();
    }
  }
</script>

<style scope>
div.detail-form {
    background: #eee;
    padding: 32px;
}
</style>