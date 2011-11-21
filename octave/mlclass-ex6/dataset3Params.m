function [C, sigma] = dataset3Params(X, y, Xval, yval)
%EX6PARAMS returns your choice of C and sigma for Part 3 of the exercise
%where you select the optimal (C, sigma) learning parameters to use for SVM
%with RBF kernel
%   [C, sigma] = EX6PARAMS(X, y, Xval, yval) returns your choice of C and 
%   sigma. You should complete this function to return the optimal C and 
%   sigma based on a cross-validation set.
%

% You need to return the following variables correctly.1, 0.3
C = 0;
sigma = 0;

% ====================== YOUR CODE HERE ======================
% Instructions: Fill in this function to return the optimal C and sigma
%               learning parameters found using the cross validation set.
%               You can use svmPredict to predict the labels on the cross
%               validation set. For example, 
%                   predictions = svmPredict(model, Xval);
%               will return the predictions on the cross validation set.
%
%  Note: You can compute the prediction error using 
%        mean(double(predictions ~= yval))
%

%generate perms of C and sigma values to optimize

multipier = 3;
seed = 0.01;
seedarr = [seed, 3*seed];
paramsvector = [seedarr, 10.* seedarr, 100.*seedarr, 1000.*seedarr](:);

%generate cartesian product 
paramsvector = cartprod(paramsvector, paramsvector);

%gather errors here
errorvector = zeros(length(paramsvector), 1);

%iterate over params vector, fetch C and sigma values
for i = 1 : length(paramsvector)
        tempC = paramsvector(i, 1);
        tempSigma = paramsvector(i, 2);
        
        fprintf('\nIteration : %d', i);
        fprintf('\nChecking for C : %f  and sigma : %f', tempC, tempSigma ); 
        
        % train the model
        tempmodel= svmTrain(X, y, tempC, @(x1, x2) gaussianKernel(x1, x2, tempSigma));

        % get predictions
        predictions = svmPredict(tempmodel, Xval);

        % calculate error
        err = mean(double(predictions ~= yval))
        
        errorvector(i) = err;
end

%get values with min error
[minerror, minindex] = min(errorvector);

C = paramsvector(minindex, 1);
sigma = paramsvector(minindex, 2)

fprintf('\n Optimal values: %f  and  %f', C, sigma);






% =========================================================================

end
