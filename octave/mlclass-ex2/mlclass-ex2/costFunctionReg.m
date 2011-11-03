function [J, grad] = costFunctionReg(theta, X, y, lambda)
%COSTFUNCTIONREG Compute cost and gradient for logistic regression with regularization
%   J = COSTFUNCTIONREG(theta, X, y, lambda) computes the cost of using
%   theta as the parameter for regularized logistic regression and the
%   gradient of the cost w.r.t. to the parameters. 

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
temp = 0;

%compute Cost J

%first part
for i = 1:m
	t = X(i, :);
	temp = temp + ( -y(i) * log(sigmoid(theta' * t')) - (1 - y(i)) * log(1 - sigmoid(theta' * t')) );
end

%second part - get the sqaures of thetas
trimtheta = theta(2:size(theta), :);
squared = trimtheta' * trimtheta;

J = (temp/ m) + (lambda * squared)/(2*m);

%fprintf('\nValue of J: %f', J);

%compute theta here
lentheta = length(theta);
temp = zeros(lentheta, 1);	

%for j = 0	
		complexterm = 0;
		complexterm1 = 0;
		
		for i = 1:m
			t = X(i, :);
			complexterm = (sigmoid(theta' * t') - y(i));
			
			temp(1) = temp(1) + complexterm * X(i, 1);			
		end
	
%sprintf('theta 0 : %f\n', temp(1));
	
		%theta(1) = (1/m)*temp(1);
	for j = 2: lentheta	
		for k = 1:m
			z = X(k, :);
			complexterm1 = (sigmoid(theta' * z') - y(k));
			temp(j) = temp(j) + (complexterm1 * X(k, j)) ;
		end	
		temp(j) = temp(j) + (lambda * theta(j));
	end	

grad = (1/m) * temp;

%sprintf('Grdianet: %f\n',grad)

% =============================================================

end
