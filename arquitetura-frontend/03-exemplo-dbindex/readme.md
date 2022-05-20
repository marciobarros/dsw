# Exemplo 03: Indexed DB

Este exemplo demonstra o uso de uma base de dados **IndexedDB** para armazenar os dados das cotações das commodities mencionadas no exemplo 02. O acesso ao banco de dados é implementado em um **singleton** e realizado em dois **Web Workers**, sendo um responsável pela atualização dos dados e outro para consulta.

**IMPORTANTE**: Você precisa ter estudado o primeiro e o segundo exemplos, sobre o uso de **local storage** e **Web Workers** no navegador, antes de analisar este exemplo.

A página Web não funciona se for carregada através do protocolo de arquivo (```file://```). É necessário criar um servidor HTTP para servir a página. O link abaixo apresenta uma forma fácil de instalar um servidor HTTP usando Node.JS.

https://jasonwatmore.com/post/2016/06/22/nodejs-setup-simple-http-server-local-web-server

Os arquivos com os preços utilizados no gráfico estão disponíveis no ```GitHub pages```. Para mais informações, clique no link abaixo:

https://pages.github.com/

Outro ótimo tutorial sobre o uso de **Indexed DB** pode ser encontrado abaixo:

https://golb.hplar.ch/2018/01/IndexedDB-programming-with-Dexie-js.html