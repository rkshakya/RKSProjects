function [J, grad] = costFunction(theta, X, y)
%COSTFUNCTION Compute cost and gradient for logistic regression
%   J = COSTFUNCTION(theta, X, y) computes the cost of using theta as the
%   parameter for logistic regression and the gradient of the cost
%   w.r.t. to the parameters.

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost of a particular choice of theta.
%               You should set J to the cost.
%               Compute the partial derivatives and set grad to the partial
%               derivatives of the cost w.r.t. each parameter in theta
%
% Note: grad should have the same dimensions as theta
%

temp = 0;

%compute Cost J

for i = 1:m
	t = X(i, :);
	temp = temp + ( -y(i) * log(sigmoid(theta' * t')) - (1 - y(i)) * log(1 - sigmoid(theta' * t')) );
end

J = temp/ m;

%fprintf('\nValue of J: %f', J);

%compute theta here
lentheta = length(theta);
temp = zeros(lentheta, 1);	

%for j = 0	
		complexterm = 0;

		for i = 1:m
			t = X(i, :);
			complexterm = (sigmoid(theta' * t') - y(i));
			
			for j = 1:lentheta				
					temp(j) = temp(j) + complexterm * X(i, j);				
			end						
		end

grad = (1/m) * temp;

% =============================================================

end
