<template>
    <div v-show="$root.credentials" class="mt-8">
        <v-container>
            <v-row>
                <v-col cols="6">
                    <h2>Remove receita</h2>
                </v-col>
                <v-col cols="6" class="text-right">
                    <v-btn color="error" class="mb-2 mr-2" @click="removeReceita">
                        Remove
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
                    <p class="view-label">ID:</p>
                    <p class="view-data">{{controlador.itemSelecionado._id}}</p>

                    <p class="view-label">Nome:</p>
                    <p class="view-data">{{controlador.itemSelecionado.nome}}</p>

                    <p class="view-label">Modo de preparo:</p>
                    <p class="view-data">{{controlador.itemSelecionado.preparo}}</p>

                    <p class="view-label mt-8 mb-1">Ingredientes:</p>
                    <v-data-table :headers="headers" :items="controlador.itemSelecionado.ingredientes" hide-default-footer class="elevation-1"></v-data-table>
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
            ],

            item: null,
        
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
            this.item = this.controlador.itemSelecionado;
        },

        removeReceita: function() {
            axios.delete("http://localhost:3000/receitas/" + this.item._id, this.httpOptions)
                .then(() => {
                    this.$router.replace('/receitas');
                });
        },

        retornaLista: function() {
            this.$router.replace('/receitas');
        }
    },

    created() {
        this.prepara();
    }
  }
</script>

<style scoped>
p.view-label {
    margin-bottom: 0px;
    font-size: 0.8em;
    color: navy;
}
</style>