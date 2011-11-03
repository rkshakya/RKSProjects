function [X_norm, mu, sigma] = featureNormalize(X)
%FEATURENORMALIZE Normalizes the features in X 
%   FEATURENORMALIZE(X) returns a normalized version of X where
%   the mean value of each feature is 0 and the standard deviation
%   is 1. This is often a good preprocessing step to do when
%   working with learning algorithms.

% You need to set these values correctly
X_norm = X;
mu = zeros(1, size(X, 2));
sigma = zeros(1, size(X, 2));

% ====================== YOUR CODE HERE ======================
% Instructions: First, for each feature dimension, compute the mean
%               of the feature and subtract it from the dataset,
%               storing the mean value in mu. Next, compute the 
%               standard deviation of each feature and divide
%               each feature by it's standard deviation, storing
%               the standard deviation in sigma. 
%
%               Note that X is a matrix where each column is a 
%               feature and each row is an example. You need 
%               to perform the normalization separately for 
%               each feature. 
%
% Hint: You might find the 'mean' and 'std' functions useful.
%       

% get means of all the features
mu = mean(X);

%fprintf('Mean values \n');
%mu;

%get std devs of all the features
sigma = std(X);

%fprintf('StdDEV values \n');
%sigma


%prepare matrix to do subtraction op
sub = ones(size(X, 1), 1) * mu;
X_norm = X - sub;

%divide by STDDEVs
div = ones(size(X, 1), 1) * sigma;
X_norm = X_norm ./ div

%fprintf('First 10 examples from the dataset after mean proc: \n');
%fprintf(' x = [%.4f %.4f]', [X(1:10,:)]');








% ============================================================

end
