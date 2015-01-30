
angular.module('conferenceschedule').controller('NewSessionController', function ($scope, $location, locationParser, SessionResource , ConferenceResource, SpeakerResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.session = $scope.session || {};
    
    $scope.conferenceList = ConferenceResource.queryAll(function(items){
        $scope.conferenceSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.name
            });
        });
    });
    $scope.$watch("conferenceSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.session.conference = {};
            $scope.session.conference.id = selection.value;
        }
    });
    
    $scope.speakersList = SpeakerResource.queryAll(function(items){
        $scope.speakersSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.name
            });
        });
    });
    $scope.$watch("speakersSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.session.speakers = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.session.speakers.push(collectionItem);
            });
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Sessions/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        SessionResource.save($scope.session, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Sessions");
    };
});