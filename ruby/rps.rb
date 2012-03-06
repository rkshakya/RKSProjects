class WrongNumberOfPlayersError < StandardError; end
class NoSuchStrategyError < StandardError; end

def rps_game_winner(game)
	raise WrongNumberOfPlayersError unless game.length == 2
	
	game.each do |g|
		if (g[1] =~ /[RPS]/i) == nil 
			raise NoSuchStrategyError
		end
	end

	# R > S, S > P, P > R
	#get the strategies
	str1 = (game[0])[1].downcase
	str2 = (game[1])[1].downcase

	case str1
		when "r" then
			if str2 == "s"
			 	return game[0]
			elsif str2 == "p"
				return game[1]
			else
				return game[0]
			end

		when "s" then
			if str2 == "p"
				return game[1]
			elsif str2 == "r"
				return game[1]
			else
				return game[0]
			end
		# R > S, S > P, P > R
		when "p" then
			if str2 == "r"
				return game[0]
			elsif str2 == "s"
				return game[1]
			else
				return game[0]
			end
	end
end

#print rps_game_winner([["Ravi", "J"], ["Sujit", "O"]])
