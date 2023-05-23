<template lang="html">
    <div v-show="$root.credentials" class="mt-8">
        <v-container>
            <v-row>
                <v-col cols="9">
                    <h2>Receitas</h2>
                </v-col>
                <v-col cols="3" class="text-right">
                    <v-btn color="primary" dark class="mb-2" @click="novaReceita">
                        Nova receita
                    </v-btn>
                </v-col>
            </v-row>
        </v-container>

        <v-container>
            <v-row>
                <v-col cols="12">
                    <div class="filter-section">
                        <v-text-field v-model="filtroNome" label="Filtro de nome" @keyup="atualizaLista"></v-text-field>
                    </div>

                    <v-data-table
                        :headers="headers"
                        :items="items"
                        :loading="loading"
                        :options.sync="options"
                        :server-items-length="totalItems"
                        class="elevation-1"
                        loading-text="Carregando os dados ... Por favor, aguarde."
                        @update:options="atualizaLista">

                        <template v-slot:[`item.actions`]="{item}">
                            <div class="text-right">
                                <v-icon class="mr-2" @click="editaReceita(item)">mdi-pencil</v-icon>
                                <v-icon @click="removeReceita(item)">mdi-delete</v-icon>
                            </div>
                        </template>
                    </v-data-table>
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
                { text: 'ID', align: 'start', sortable: true, value: '_id' },
                { text: 'Nome', align: 'start', sortable: true, value: 'nome' },
                { text: '', value: 'actions', sortable: false },
            ],
            items: [],
            totalItems: 0,
            loading: false,
            options: { itemsPerPage: 10 },
            filtroNome: '',
        
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
        atualizaLista: function() {
            this.loading = true;

            var sortBy = (this.options.sortBy && this.options.sortBy.length > 0) ? this.options.sortBy[0] : "";
            var sortDesc = (this.options.sortDesc && this.options.sortDesc.length > 0) ? this.options.sortDesc[0] : "";
            var page = this.options.page;
            var itemsPerPage = this.options.itemsPerPage;
            var queryString = `sortField=${sortBy}&sortDesc=${sortDesc}&page=${page}&itemsPage=${itemsPerPage}&filter=${this.filtroNome}`;

            axios.get(this.$root.config.url + "/receitas?" + queryString, this.httpOptions)
                .then(response => {
                    this.items = response.data.items;
                    this.totalItems = response.data.totalItems;
                    this.loading = false;
                })
                .catch(error => {
                    this.error = error.response.data.message;
                })
        },

        novaReceita: function() {
            this.controlador.setItemSelecionado({ _id: "", nome: '', preparo: '', ingredientes: [] });
            this.$router.replace('/receitas/edit');
        },

        editaReceita: function(item) {
            this.controlador.setItemSelecionado(item);
            this.$router.replace('/receitas/edit');
        },

        removeReceita: function(item) {
            this.controlador.setItemSelecionado(item);
            this.$router.replace('/receitas/view');
        }
    },

    created() {
        console.log(this.controlador.title);
        this.atualizaLista();
    }
  }
</script>

<style scoped>
.filter-section {
    background-color: #eee;
    border: 1px solid #ccc;
    padding: 4px 8px;
    margin-bottom: 16px;
}
</style>