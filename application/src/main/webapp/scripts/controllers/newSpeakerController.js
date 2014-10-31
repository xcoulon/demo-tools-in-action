
angular.module('conferences').controller('NewSpeakerController', function ($scope, $location, locationParser, SpeakerResource , SessionResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.speaker = $scope.speaker || {};
    
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
            $scope.speaker.sessions = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.speaker.sessions.push(collectionItem);
            });
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Speakers/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        SpeakerResource.save($scope.speaker, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Speakers");
    };
});