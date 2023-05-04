const mongoose = require('mongoose');
var url = 'mongodb+srv://user-test:Ab123456@cluster0.5tlslxx.mongodb.net/exemplo?retryWrites=true&w=majority';

mongoose.connect(url).then(() => {
    const db = mongoose.connection;
    db.on("error", console.error.bind(console, "MongoDB connection error:"));
    
    const Receita = mongoose.model('Receita', { 
		prato: String,
		descricao: String,
		ingredientes: [{
			item : String,
			qtde : Number
		}]
	});
		    
    const primeira = new Receita({
		prato: "Arroz com feijão",
		descricao: "cozinhe o arroz por ...",
		ingredientes: [{
			item: "arroz branco",
			qtde: 200
		}, {
			item: "feijão preto",
			qtde: 200
		}]
	});
    
    primeira.save().then(() => {
        db.close();
    });
});
