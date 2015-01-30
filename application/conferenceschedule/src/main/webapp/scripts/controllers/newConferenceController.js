
angular.module('conferenceschedule').controller('NewConferenceController', function ($scope, $location, locationParser, ConferenceResource , SessionResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.conference = $scope.conference || {};
    
    $scope.sessionsList = SessionResource.queryAll(function(items){
        $scope.sessionsSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.id
            });
        });
    });
    $scope.$watch("sessionsSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.conference.sessions = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.conference.sessions.push(collectionItem);
            });
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Conferences/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        ConferenceResource.save($scope.conference, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Conferences");
    };
});