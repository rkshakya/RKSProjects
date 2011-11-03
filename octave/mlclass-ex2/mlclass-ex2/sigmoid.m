function g = sigmoid(z)
%SIGMOID Compute sigmoid functoon
%   J = SIGMOID(z) computes the sigmoid of z.

% You need to return the following variables correctly 
g = zeros(size(z));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the sigmoid of each value of z (z can be a matrix,
%               vector or scalar).

[row, col] = size(z);

%fprintf('INdices %d %d\n', row, col);

for rowitr = 1: row
	for colitr = 1: col
		temp = 1/(1 + exp(-z(rowitr, colitr)) );
		g(rowitr, colitr) = temp;
	end
end


% =============================================================

end
