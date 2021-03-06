function [J, grad] = linearRegCostFunction(X, y, theta, lambda)
%LINEARREGCOSTFUNCTION Compute cost and gradient for regularized linear 
%regression with multiple variables
%   [J, grad] = LINEARREGCOSTFUNCTION(X, y, theta, lambda) computes the 
%   cost of using theta as the parameter for linear regression to fit the 
%   data points in X and y. Returns the cost in J and the gradient in grad

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost and gradient of regularized linear 
%               regression for a particular choice of theta.
%
%               You should set J to the cost and grad to the gradient.

% calculate hypothesis
h_theta = X * theta;
h_theta = h_theta - y;


%sum of squared error term
err = sum(h_theta  .^ 2);

% regularization term - exclude theta 1
reg = theta(2: end, :);
regsum = sum (reg .^ 2);

J = (1/(2*m)) * err  + (lambda / (2 * m)) * regsum;

%calculate gradients now
grad_temp = h_theta' * X;
vec_grad = (1/m) * (grad_temp');

% prepare for regularization
reg_vec = theta;
reg_vec(1) = 0;

reg_vec = (lambda/m) * reg_vec;

grad = vec_grad + reg_vec;
















% =========================================================================

grad = grad(:);

end
