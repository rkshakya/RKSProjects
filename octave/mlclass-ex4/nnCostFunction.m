function [J grad] = nnCostFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, ...
                                   X, y, lambda)
%NNCOSTFUNCTION Implements the neural network cost function for a two layer
%neural network which performs classification
%   [J grad] = NNCOSTFUNCTON(nn_params, hidden_layer_size, num_labels, ...
%   X, y, lambda) computes the cost and gradient of the neural network. The
%   parameters for the neural network are "unrolled" into the vector
%   nn_params and need to be converted back into the weight matrices. 
% 
%   The returned parameter grad should be a "unrolled" vector of the
%   partial derivatives of the neural network.
%

% Reshape nn_params back into the parameters Theta1 and Theta2, the weight matrices
% for our 2 layer neural network
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));

% Setup some useful variables
m = size(X, 1);
         
% You need to return the following variables correctly 
J = 0;
Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));

% ====================== YOUR CODE HERE ======================
% Instructions: You should complete the code by working through the
%               following parts.
%
% Part 1: Feedforward the neural network and return the cost in the
%         variable J. After implementing Part 1, you can verify that your
%         cost function computation is correct by verifying the cost
%         computed in ex4.m
%
% Part 2: Implement the backpropagation algorithm to compute the gradients
%         Theta1_grad and Theta2_grad. You should return the partial derivatives of
%         the cost function with respect to Theta1 and Theta2 in Theta1_grad and
%         Theta2_grad, respectively. After implementing Part 2, you can check
%         that your implementation is correct by running checkNNGradients
%
%         Note: The vector y passed into the function is a vector of labels
%               containing values from 1..K. You need to map this vector into a 
%               binary vector of 1's and 0's to be used with the neural network
%               cost function.
%
%         Hint: We recommend implementing backpropagation using a for-loop
%               over the training examples if you are implementing it for the 
%               first time.
%
% Part 3: Implement regularization with the cost function and gradients.
%
%         Hint: You can implement this around the code for
%               backpropagation. That is, you can compute the gradients for
%               the regularization separately and then add them to Theta1_grad
%               and Theta2_grad from Part 2.
%


% add additional 1
X = [ones(m, 1), X];

%var to gather cumulative cost
temp = 0;

for i = 1 : m
       %calcualte h_theta as a vector
        z_2 = Theta1 * (X(i, :))';
        a_2 = sigmoid(z_2);
        a_2 = [1; a_2];
        z_3 = Theta2 * a_2;
        h_theta = sigmoid(z_3);
        
        %prepare y vector
        temp_y = zeros(1, num_labels);
        temp_y( y(i) ) = 1;
        temp_y = temp_y(:);
        
        temp = temp + sum( (temp_y .* log(h_theta)) + ((1 - temp_y).* log(1 - h_theta)) );
        
end 

J = -temp/m;

% prepare for regularization
trim_1 = Theta1 .^ 2;  % exclude col1 from reg
part_1 = trim_1(:, 2:end);
part_1 = sum(sum(part_1), 2);

trim_2 = Theta2 .^ 2;  % exclude col1 from reg
part_2 = trim_2(:, 2:end);
part_2 = sum(sum(part_2), 2);


J = J + (lambda/(2 * m)) * (part_1 + part_2);


% work for gradients

%iterate over each trng example (TODO: see if above loop for cost can be reused)
for i = 1 : m
        
       %STEP1: calculate params using feedforward activation 
       %calcualte h_theta as a vector
        z_2 = Theta1 * (X(i, :))';
        a_2 = sigmoid(z_2);
        a_2 = [1; a_2];
        z_3 = Theta2 * a_2;
        h_theta = sigmoid(z_3);
        
        %prepare y vector
        temp_y = zeros(1, num_labels);
        temp_y( y(i) ) = 1;
        temp_y = temp_y(:);
        
        %STEP 2: calculate errors(small deltas) for nodes
        delt_3 = h_theta - temp_y;
        
        %STEP 3: propagate deltas to hidden layers, need 25 x 1 matrix, exclude 1st col from Theta2
        delt_2 = ( (Theta2(:, 2:end))' * delt_3) .* sigmoidGradient(z_2);
        
        %STEP 4 : accumulate gradients
        Theta1_grad = Theta1_grad + delt_2 * X(i, :);
        Theta2_grad = Theta2_grad + delt_3 * (a_2)';
        
end 


Theta1_grad = (1/m) .* Theta1_grad;
Theta2_grad = (1/m) .* Theta2_grad;

%implement regularization for gradients

%prepare temp matrices
temp_theta1_grad = (lambda/m) .* Theta1;
temp_theta2_grad = (lambda/m) .* Theta2;

%set 1st col to 0
temp_theta1_grad(:, 1) = 0;
temp_theta2_grad(:, 1) = 0;

Theta1_grad = Theta1_grad + temp_theta1_grad;
Theta2_grad = Theta2_grad + temp_theta2_grad;

% -------------------------------------------------------------

% =========================================================================

% Unroll gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];


end
