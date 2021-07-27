var vl = require("./getData.js");

async function y(){
    let y = await vl.getYData();
    console.log(y)
    return 1;
}


y();
