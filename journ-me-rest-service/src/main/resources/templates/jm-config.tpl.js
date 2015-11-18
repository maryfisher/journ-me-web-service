(function appScope(angular) {

var app = angular.module("jm-config", []);

app.constant("jmCategories", 
    ${categoriesObject}
)
.constant("jmStates",
    ${statesObject}
);

}(window.angular));