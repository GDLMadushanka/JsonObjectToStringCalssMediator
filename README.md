# JsonObjectToStringCalssMediator
Class mediator which converts all elements of a JSON object to string

Convert 
```
{
  "fruit"           : "12345",
  "price"           : 7.5,
  "simpleObject"    : {"age":234},
  "simpleArray"     : [true,false,true],
  "objWithArray"    : {"marks":[34,45,56,67]},
  "arrayOfObjects"  : [{"maths":90},{"physics":95},{"chemistry":65}],
  "nestedObject"    : {"Lahiru" :{"age":27},"Nimal" :{"married" :true}, "Kamal" : {"scores": [24,45,"67"]}},
  "nestedArray"     : [[12,"23",34],["true",false],["Linking Park","Coldplay"]],
  "allNumericArray" : [3,1,4],
  "nullArray"       : ["",null,"null"]
}
```
to

```
{
  "fruit": "12345",
  "price": "7.5",
  "simpleObject": {
    "age": "234"
  },
  "simpleArray": [
    "true",
    "false",
    "true"
  ],
  "objWithArray": {
    "marks": [
      "34",
      "45",
      "56",
      "67"
    ]
  },
  "arrayOfObjects": [
    {
      "maths": "90"
    },
    {
      "physics": "95"
    },
    {
      "chemistry": "65"
    }
  ],
  "nestedObject": {
    "Lahiru": {
      "age": "27"
    },
    "Nimal": {
      "married": "true"
    },
    "Kamal": {
      "scores": [
        "24",
        "45",
        "67"
      ]
    }
  },
  "nestedArray": [
    [
      "12",
      "23",
      "34"
    ],
    [
      "true",
      "false"
    ],
    [
      "Linking Park",
      "Coldplay"
    ]
  ],
  "allNumericArray": [
    "3",
    "1",
    "4"
  ],
  "nullArray": [
    "",
    null,
    "null"
  ]
}
```
