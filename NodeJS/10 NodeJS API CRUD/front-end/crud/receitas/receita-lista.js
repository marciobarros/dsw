Vue.component('lista-receitas', {
    props: ['controlador'],

    data: function () {
        return { 
            headers: [
                { text: 'ID', align: 'start', sortable: true, value: 'id' },
                { text: 'Nome', align: 'start', sortable: true, value: 'nome' },
                { text: '', value: 'actions', sortable: false },
            ],
            items: [],
            totalItems: 0,
            loading: false,
            options: { itemsPerPage: 10 },
            filtroNome: ''
        }
    },
    
    template: `
        <div>
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
                            <label for="filter-nome">Nome:</label>
                            <input name="filter-nome" v-model="filtroNome" @keyup="atualizaLista"/>
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

                            <template v-slot:item.actions="{item}">
                                <div class="text-right">
                                    <v-icon class="mr-2" @click="editaReceita(item)">mdi-pencil</v-icon>
                                    <v-icon @click="removeReceita(item)">mdi-delete</v-icon>
                                </div>
                            </template>
                        </v-data-table>
                    </v-col>
                </v-row>
            </v-container>
        </div>`,

    methods: {
        atualizaLista: function() {
            this.loading = true;

            var sortBy = (this.options.sortBy && this.options.sortBy.length > 0) ? this.options.sortBy[0] : "";
            var sortDesc = (this.options.sortDesc && this.options.sortDesc.length > 0) ? this.options.sortDesc[0] : "";
            var page = this.options.page;
            var itemsPerPage = this.options.itemsPerPage;
            var queryString = `sortField=${sortBy}&sortDesc=${sortDesc}&page=${page}&itemsPage=${itemsPerPage}&filter=${this.filtroNome}`;

            axios.get("http://localhost:3000/receitas?" + queryString).then(response => {
              this.items = response.data.items;
              this.totalItems = response.data.totalItems;
              this.loading = false;
            })
        },

        novaReceita: function() {
            this.controlador.insere({ id: -1, nome: '', preparo: '', ingredientes: [] });
        },

        editaReceita: function(item) {
            this.controlador.edita(item);
        },

        removeReceita: function(item) {
            this.controlador.remove(item);
        }
    }
})