const delimiters = ['_#', '_@', '.'];
//const example_string = 'FMvy3dnaMAEAkS4_#HuTao_#GenshinImpact_@POISE_21.jpg';
//const fileList = ["FPjAxF4aIAMyMTs_Spring_@2GONG_02.jpg","FMvy3dnaMAEAkS4_#HuTao_#GenshinImpact_@POISE_21.jpg","FPfXafmaQAEHRUS_@POISE_21.jpg","FPfXYsCaIAIbBtv_@POISE_21.jpg","FPKq-zTVUAEHQZG_#BlueArchive_@hipanan2222.jpg","FPP5r62aUA0Kh6W_Ninja_Pekora_@Sco_ttie.jpg","FPP6dDnaMAYtj3M_@_HaeO.jpg","FPPm169agAQNSFF_Late_@EunYooo_.jpg","FPPOkuJVQAEnUEA_@Nannung_mdr.jpg","FPPqE19VEAQYCNG_@Tess88884.jpg","FPQAoyqVIAkksrk_Yeon[CherryBlossom]_@rmflawha12.jpg","FPQEUZTaUAc-EGd_Spring_@ozzingo.jpg","FPQKCt6aUAQfnkq_Spring_@_NknHm.jpg","FPR8bqhVIAQMACj_#Yelan_@retty9349.jpg","FPT0M6fVQAg2aeh_Ookami_Mio_@Yozora1902.jpg","FPT8Rz9UYAQYDIq_@hd_1735.jpg","FPUZpSkaQAEZlUJ_Mars_@patch_oxxo.jpg"];

const fs = require('fs');
const fileList = fs.readdirSync('C:\\Users\\joan_\\OneDrive\\Images\\Albums\\Twitter');
//console.log('Nb files = '+fileList.length);

//---------- Parser ----------
/**
 * Recursively construct a list of delimiters and texts from a string.
 * @param str the source string
 * @param previous_index last index found at the end of a delimiter
 * @param current_index current index for parsing
 * @param result the list of {type, value} with type being either 'D' for delimiter or 'T' for text.
 */
function tokenize(str, previous_index, current_index, result) {
    // end of recursive function
    if (current_index === str.length) {
        result.push({type:'T', value:str.substring(previous_index)});
        return;
    }
    // find if the current index points to a delimiter
    let delim = delimiters.find(d => str.substring(current_index, current_index + d.length) === d);
    if (delim) {
        // new delimiter found, adding the preceding text between both indexes
        if (current_index > 0) result.push({type:'T', value: str.substring(previous_index, current_index)});
        // add the new delimiter
        result.push({type:'D', delim});
        tokenize(str, current_index+delim.length, current_index+delim.length, result);
    } else {
        tokenize(str, previous_index, current_index + 1, result);
    }
}

/**
 * Create couples of {delimiter, text} into an accumulator array.
 * @param acc the accumulator
 * @param cur current {type, value} of either a delimiter or a text
 * @returns the accumulator
 */
function meetic(acc, cur) {
    if (cur.type === 'D') acc.push({delim: cur.delim});
    if (cur.type === 'T') acc.slice(-1)[0].text = cur.value;
    return acc;
}

// Generate the full list of values {delimiter, text}
const couples = fileList.reduce( (acc, filename) => {
    let tokens = [];
    tokenize(filename, 0, 0, tokens); // one string into tokens [text, delimiter, text ...]
    tokens.shift(); // remove the start of line
    tokens.reduce( (a, t) => meetic(a, t), acc); // one string into couples of {delimiter, text}
    return acc;
}, []);

//---------- Segmentation ----------

// Initialize the following reduce accumulator
//const init = new Map();
//delimiters.forEach(d => init[d] = new Map());
const init = {};
delimiters.forEach(d => init[d] = {});

const result = couples.reduce( (acc, cur) => {
    let val = acc[cur.delim][cur.text];
    val = val ? val + 1 : 1;
    acc[cur.delim][cur.text] = val;
    return acc;
}, init);

//---------- Sort mechanism ----------
// from https://stackoverflow.com/questions/17684921/sort-json-object-in-javascript

function isObject(v) {
    return '[object Object]' === Object.prototype.toString.call(v);
};

JSON.sort = function(o) {
    if (Array.isArray(o)) {
        return o.sort().map(JSON.sort);
    } else if (isObject(o)) {
        return Object
            .keys(o)
            .sort( (k1,k2) => o[k2] - o[k1])
            .reduce(function(a, k) {
                a[k] = JSON.sort(o[k]);
                return a;
            }, {});
    }

    return o;
}

//---------- Display ----------

//console.log(JSON.sort(result)); // display in JSON format, using the code snippet above as sorting routine.
// Display as indented text
Object.keys(result).forEach( delim => {
    console.log(delim);
    let map = result[delim];
    Object.keys(map).sort((k1,k2) => map[k2] - map[k1]).forEach( (k,v) => console.log('\t'+map[k]+'\t'+k));
});

//---------- An example of map sorting ----------
// function logMapElements(v, k, map) {
//     console.log(`('${v}') ${k}`);
// }
// const m = new Map([["toto", 3], ["truc", 6], ["bidule", 8]]);
// const sm = new Map([...m.entries()].sort((a, b) => b[1] - a[1]));
// sm.forEach(logMapElements);
