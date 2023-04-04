const express = require('express')
const app = express()

app.get('/', (req, res, next) => {
  let xml = `<?xml version="1.0" encoding="UTF-8"?>
             <library>
               <album>
                  <title>Love over Gold</title>
                  <band>Dire Straits</band>
               </album>
               <album>
                  <title>Joshua Tree</title>
                  <band>U2</band>
               </album>
               <album>
                  <title>Seven and the Ragged Tiger</title>
                  <band>Duran Duran</band>
               </album>
               <album>
                  <title>Delicate Sound of the Thunder</title>
                  <band>Pink Floyd</band>
               </album>
             </library>`

  res.header('Content-Type', 'application/xml')
  res.status(200).send(xml)
})

app.listen(3000);