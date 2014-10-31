

angular.module('conferences').controller('EditSpeakerController', function($scope, $routeParams, $location, SpeakerResource , SessionResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.speaker = new SpeakerResource(self.original);
            SessionResource.queryAll(function(items) {
                $scope.sessionsSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.id
                    };
                    if($scope.speaker.sessions){
                        $.each($scope.speaker.sessions, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.sessionsSelection.push(labelObject);
                                $scope.speaker.sessions.push(wrappedObject);
                            }
                        });
                        self.original.sessions = $scope.speaker.sessions;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/Speakers");
        };
        SpeakerResource.get({SpeakerId:$routeParams.SpeakerId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.speaker);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.speaker.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Speakers");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Speakers");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.speaker.$remove(successCallback, errorCallback);
    };
    
    $scope.sessionsSelection = $scope.sessionsSelection || [];
    $scope.$watch("sessionsSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.speaker) {
            $scope.speaker.sessions = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.speaker.sessions.push(collectionItem);
            });
        }
    });
    
    $scope.get();
});