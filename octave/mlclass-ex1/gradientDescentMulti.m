function [theta, J_history] = gradientDescentMulti(X, y, theta, alpha, num_iters)
%GRADIENTDESCENTMULTI Performs gradient descent to learn theta
%   theta = GRADIENTDESCENTMULTI(x, y, theta, alpha, num_iters) updates theta by
%   taking num_iters gradient steps with learning rate alpha

% Initialize some useful values
m = length(y); % number of training examples
J_history = zeros(num_iters, 1);
lentheta = length(theta);

for iter = 1:num_iters

    % ====================== YOUR CODE HERE ======================
    % Instructions: Perform a single gradient step on the parameter vector
    %               theta. 
    %
    % Hint: While debugging, it can be useful to print out the values
    %       of the cost function (computeCostMulti) and gradient here.
    %
	
	% iterate to make program feature independent
	%for it = 1: lentheta		
		temp = zeros(lentheta, 1);		
		complexterm = 0;

		for i = 1:m
			t = X(i, :);
			complexterm = (theta' * t' - y(i));
			
			for j = 1:lentheta
				temp(j) = temp(j) + complexterm * X(i, j);
			end
			
		end

		%theta0 = theta0 - (alpha/m) * temp;
		%theta1 = theta1 - (alpha/m) * temp1;
		%theta2 = theta2 - (alpha/m) * temp2;

		%theta(1) = theta0;
		%theta(2) = theta1;
		%theta(3) = theta2;
		
		theta = theta - (alpha/m)*temp;

	%end

    % ============================================================

    % Save the cost J in every iteration    
    J_history(iter) = computeCostMulti(X, y, theta);

end

end
