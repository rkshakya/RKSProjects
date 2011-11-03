function [theta, J_history] = gradientDescent(X, y, theta, alpha, num_iters)
%GRADIENTDESCENT Performs gradient descent to learn theta
%   theta = GRADIENTDESENT(X, y, theta, alpha, num_iters) updates theta by 
%   taking num_iters gradient steps with learning rate alpha

% Initialize some useful values
m = length(y); % number of training examples
J_history = zeros(num_iters, 1);

for iter = 1:num_iters

    % ====================== YOUR CODE HERE ======================
    % Instructions: Perform a single gradient step on the parameter vector
    %               theta. 
    %
    % Hint: While debugging, it can be useful to print out the values
    %       of the cost function (computeCost) and gradient here.
    %
	theta0 = theta(1);
	theta1 = theta(2);
	
	temp = 0;
	temp1 = 0;
	complexterm = 0;

	for i = 1:m
		t = X(i, :);
		complexterm = (theta' * t' - y(i));
		temp = temp + complexterm;
		temp1 = temp1 + complexterm * X(i,2);
	end

	theta0 = theta0 - (alpha/m) * temp;
	theta1 = theta1 - (alpha/m) * temp1;

	theta(1) = theta0;
	theta(2) = theta1;


    % ============================================================
	%computeCost(X, y, theta)

    % Save the cost J in every iteration    
    J_history(iter) = computeCost(X, y, theta);

end

end
