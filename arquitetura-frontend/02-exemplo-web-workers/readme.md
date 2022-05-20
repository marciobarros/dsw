# Exemplo 02: Web Workers

Este exemplo demonstra o uso de um **Web Worker** para rodar um programa JavaScript em uma *thread* separada do programa principal. Para tarefas que exigem um tempo de processamento mais longo, isto impede que a interface com o usuário fique *congelada* enquanto o processamento é realizado. 

*IMPORTANTE*: Você precisa ter estudado o primeiro exemplo, sobre o uso de **local storage** no navegador, antes de analisar este exemplo.

A página Web não funciona se for carregada através do protocolo de arquivo (```file://```). É necessário criar um servidor HTTP para servir a página. O link abaixo apresenta uma forma fácil de instalar um servidor HTTP usando Node.JS.

https://jasonwatmore.com/post/2016/06/22/nodejs-setup-simple-http-server-local-web-server

Os arquivos com os preços utilizados no gráfico estão disponíveis no ```GitHub pages```. Para mais informações, clique no link abaixo:

https://pages.github.com/


Outro exemplo interessante de uso de Web Workers pode ser encontrado abaixo:

https://dev.to/trezy/loading-images-with-web-workers-49ap
