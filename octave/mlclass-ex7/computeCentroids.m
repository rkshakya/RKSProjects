function centroids = computeCentroids(X, idx, K)
%COMPUTECENTROIDS returs the new centroids by computing the means of the 
%data points assigned to each centroid.
%   centroids = COMPUTECENTROIDS(X, idx, K) returns the new centroids by 
%   computing the means of the data points assigned to each centroid. It is
%   given a dataset X where each row is a single data point, a vector
%   idx of centroid assignments (i.e. each entry in range [1..K]) for each
%   example, and K, the number of centroids. You should return a matrix
%   centroids, where each row of centroids is the mean of the data points
%   assigned to it.
%

% Useful variables
[m n] = size(X);

% You need to return the following variables correctly.
centroids = zeros(K, n);


% ====================== YOUR CODE HERE ======================
% Instructions: Go over every centroid and compute mean of all points that
%               belong to it. Concretely, the row vector centroids(i, :)
%               should contain the mean of the data points assigned to
%               centroid i.
%
% Note: You can use a for-loop over the centroids to compute this.
%


for i = 1 : K
    temp = zeros(size(idx));
    % find the indices corresp to ith cluster, use logical array
    temp = (idx == i);
    
    % no of elems associated with this cluster
    count = size(find(temp), 1);
    
    
    %replicate temp so that we can use it to directly manipulate X
    tempMod = temp;
    
    for j = 2: size(X, 2)
        tempMod = [tempMod, temp];
    end   

    %fprintf('Count: %d', count);
    %now get only the relevant rows and set others to 0
    centroids(i, :) = (1/count) .* sum(tempMod .* X, 1);
    
    
end    

% =============================================================


end

